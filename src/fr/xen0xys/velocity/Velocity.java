package fr.xen0xys.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.logging.Logger;

@Plugin(id = "xen0lib", name = "Xen0Lib", version = "1.1.0", authors = {"Xen0Xys"})
public class Velocity {

    @Inject
    public Velocity(ProxyServer server, Logger logger){
        logger.info("Starting Xen0Lib");
    }

}
