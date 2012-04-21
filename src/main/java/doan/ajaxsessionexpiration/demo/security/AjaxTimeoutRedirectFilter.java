package doan.ajaxsessionexpiration.demo.security;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.web.filter.GenericFilterBean;
import org.thymeleaf.util.StringUtils;

public class AjaxTimeoutRedirectFilter extends GenericFilterBean
{

	private static final Logger logger = LoggerFactory.getLogger(AjaxTimeoutRedirectFilter.class);

	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();
	private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

	private int customSessionExpiredErrorCode = 901;

	private String restString;

	private RestPatternMode restStringPatternMode;

	private Pattern restPattern;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		try
		{
			chain.doFilter(request, response);

			logger.debug("Chain processed normally");
		}
		catch (IOException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
			RuntimeException ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);

			if (ase == null)
			{
				ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
			}

			if (ase != null)
			{
				if (ase instanceof AuthenticationException)
				{
					throw ase;
				}
				else if (ase instanceof AccessDeniedException)
				{

					if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication()))
					{
						logger.info("User session expired or not logged in yet");
						String url = ((HttpServletRequest) request).getPathInfo();

						if (url != null && this.restPattern.matcher(url).matches())
						{
							logger.info("Ajax call detected, send {} error code", this.customSessionExpiredErrorCode);
							HttpServletResponse resp = (HttpServletResponse) response;
							resp.sendError(this.customSessionExpiredErrorCode);
						}
						else
						{
							logger.info("Redirect to login page");
							throw ase;
						}
					}
					else
					{
						throw ase;
					}
				}
			}

		}
	}

	private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer
	{
		/**
		 * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
		 */
		protected void initExtractorMap()
		{
			super.initExtractorMap();

			registerExtractor(ServletException.class, new ThrowableCauseExtractor()
			{
				public Throwable extractCause(Throwable throwable)
				{
					ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
					return ((ServletException) throwable).getRootCause();
				}
			});
		}

	}

	public void setCustomSessionExpiredErrorCode(int customSessionExpiredErrorCode)
	{
		this.customSessionExpiredErrorCode = customSessionExpiredErrorCode;
	}

	public void setRestString(String restString)
	{
		this.restString = restString;
	}

	public void setRestStringPatternMode(RestPatternMode restStringPatternMode)
	{
		this.restStringPatternMode = restStringPatternMode;
	}

	@Override
	public void afterPropertiesSet() throws ServletException
	{
		super.afterPropertiesSet();
		if (StringUtils.isEmpty(this.restString))
		{
			throw new IllegalArgumentException(" The 'restString' property should be set");
		}
		if (this.restStringPatternMode == null)
		{
			throw new IllegalArgumentException(" The 'restStringPatternMode' property should be set");
		}

		if (this.restStringPatternMode == RestPatternMode.PREFIX)
		{
			this.restPattern = Pattern.compile("^" + this.restString + ".*$");
		}
		else if (this.restStringPatternMode == RestPatternMode.SUFFIX)
		{
			this.restPattern = Pattern.compile("^.+" + this.restString + "$");
		}
		else if (this.restStringPatternMode == RestPatternMode.CONTAINS)
		{
			this.restPattern = Pattern.compile("^.+" + this.restString + ".+$");
		}
		else
		{
			throw new IllegalArgumentException(" The 'restPattern' property possible values are: {'PREFIX','SUFFIX','CONTAINS'}");
		}

	}

	public enum RestPatternMode
	{
		PREFIX,
		SUFFIX,
		CONTAINS
	}
}
