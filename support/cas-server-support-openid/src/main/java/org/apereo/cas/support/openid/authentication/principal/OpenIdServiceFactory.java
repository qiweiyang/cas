package org.apereo.cas.support.openid.authentication.principal;

import org.apereo.cas.authentication.principal.AbstractServiceFactory;
import org.apereo.cas.support.openid.OpenIdProtocolConstants;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * The {@link OpenIdServiceFactory} creates {@link OpenIdService} objects.
 *
 * @author Misagh Moayyed
 * @since 4.2
 */
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class OpenIdServiceFactory extends AbstractServiceFactory<OpenIdService> {

    private final String openIdPrefixUrl;

    @Override
    public OpenIdService createService(final HttpServletRequest request) {
        val service = request.getParameter(OpenIdProtocolConstants.OPENID_RETURNTO);
        val openIdIdentity = request.getParameter(OpenIdProtocolConstants.OPENID_IDENTITY);

        if (openIdIdentity == null || !StringUtils.hasText(service)) {
            return null;
        }

        val id = cleanupUrl(service);
        val artifactId = request.getParameter(OpenIdProtocolConstants.OPENID_ASSOCHANDLE);
        val s = new OpenIdService(id, service, artifactId, openIdIdentity);
        s.setLoggedOutAlready(true);
        s.setSource(OpenIdProtocolConstants.OPENID_RETURNTO);
        return s;
    }

    @Override
    public OpenIdService createService(final String id) {
        return new OpenIdService(id, id, null, this.openIdPrefixUrl);
    }
}
