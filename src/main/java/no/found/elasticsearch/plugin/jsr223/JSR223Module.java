package no.found.elasticsearch.plugin.jsr223;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.multibindings.Multibinder;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.script.ScriptEngineService;

public class JSR223Module extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<ScriptEngineService> multibinder = Multibinder.newSetBinder(binder(), ScriptEngineService.class);
        getClass().getClassLoader();
        ScriptEngineManager manager = new ScriptEngineManager();
        ESLogger logger = Loggers.getLogger(getClass());
        for (ScriptEngineFactory factory : manager.getEngineFactories()) {
            logger.info("Registering JSR223 engine: [{}-{}] for type: [{}] and extensions: [{}]",
                    factory.getEngineName(), factory.getEngineVersion(), factory.getLanguageName(),
                    factory.getExtensions());
            multibinder.addBinding().toInstance(new JSR223Adapter(factory));
        }
    }

}
