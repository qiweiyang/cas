package org.apereo.cas.util.transforms;

import org.apereo.cas.authentication.handler.PrincipalNameTransformer;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

/**
 * A transformer that chains a number of inner transformers together.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */
@Slf4j
@ToString
@Getter
public class ChainingPrincipalNameTransformer implements PrincipalNameTransformer {

    private static final long serialVersionUID = 7586914936775326709L;

    private List<PrincipalNameTransformer> transformers = new ArrayList<>();

    @Override
    public String transform(final String formUserId) {
        var idToTransform = formUserId;
        for (val t : this.transformers) {
            idToTransform = t.transform(idToTransform);
        }
        return idToTransform;
    }

    /**
     * Add transformer.
     *
     * @param transformer the transformer
     */
    public void addTransformer(final PrincipalNameTransformer transformer) {
        this.transformers.add(transformer);
    }
}
