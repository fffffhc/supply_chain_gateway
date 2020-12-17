package com.scf.erdos.common.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ApiIdempotentImportSelector implements ImportSelector {
    /**
     * Select and return the names of which class(es) should be imported based on
     *
     * @param importingClassMetadata
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                "com.scf.erdos.common.config.ApiIdempotentConfig"
        };
    }
}
