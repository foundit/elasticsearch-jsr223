package no.found.elasticsearch.plugin.jsr223;

import java.util.ArrayList;
import java.util.Collection;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugins.AbstractPlugin;


public class JSR223Plugin extends AbstractPlugin {
    @Override public String name() {
        return "JSR-223-plugin";
    }

    @Override public String description() {
        return "Plugin that enables any JSR-223 engine to be used as a scripting engine.";
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        Collection<Class<? extends Module>> modules = new ArrayList<Class<? extends Module>>();
        modules.add(JSR223Module.class);
        return modules;
    }
    
}
