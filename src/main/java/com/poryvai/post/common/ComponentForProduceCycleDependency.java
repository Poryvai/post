package com.poryvai.post.common;

import com.poryvai.post.service.ParcelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

/**
 * A Spring component specifically designed to demonstrate how to handle
 * <a href="https://www.baeldung.com/spring-circular-dependencies">circular dependencies</a>
 * in a Spring application context.
 * It has a dependency on {@link ParcelServiceImpl} which, in turn, may depend on this component,
 * creating a cycle. The {@code @Lazy} annotation is used to break this cycle at startup.
 */
@Component
@RequiredArgsConstructor
public class ComponentForProduceCycleDependency {
    /**
     * An instance of {@link ParcelServiceImpl}, injected lazily to prevent a circular dependency issue
     * during the Spring application context startup.
     * The {@link Lazy @Lazy} annotation ensures that {@code ParcelServiceImpl} is injected
     * as a proxy and not instantiated until it's actually needed.
     */
    private @Lazy ParcelServiceImpl parcelService;

    /**
     * A simple method that calls {@code methodForTrick()} on the injected {@link ParcelServiceImpl}.
     * This method serves primarily as a demonstration of the dependency flow within the circular setup,
     * highlighting how the lazy-loaded dependency is eventually used.
     */
    public void doNothing() {
        parcelService.methodForTrick();
    }
}
