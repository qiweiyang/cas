package org.apereo.cas.adaptors.duo.web.flow.action;

import org.apereo.cas.web.flow.resolver.CasWebflowEventResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * This is {@link DuoAuthenticationWebflowAction}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class DuoAuthenticationWebflowAction extends AbstractAction {

    private final CasWebflowEventResolver duoAuthenticationWebflowEventResolver;

    @Override
    protected Event doExecute(final RequestContext requestContext) {
        return this.duoAuthenticationWebflowEventResolver.resolveSingle(requestContext);
    }
}
