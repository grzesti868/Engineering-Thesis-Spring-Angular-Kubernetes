/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApplicationEventListener {

    @EventListener
    public void onStartup(ApplicationReadyEvent event) {
        log.info("*** Application initialised ***");
    }

    @EventListener
    public void onShutdown(ContextStoppedEvent event) {
        log.info("*** Application stopped ***");

    }

}