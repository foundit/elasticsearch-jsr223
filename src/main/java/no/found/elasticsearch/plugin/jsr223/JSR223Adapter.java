package no.found.elasticsearch.plugin.jsr223;

import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.ScriptEngineService;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;

public class JSR223Adapter implements ScriptEngineService {
    private final ScriptEngineFactory factory;
    private final Compilable compiler;
    private final ESLogger logger = Loggers.getLogger(getClass());

    public JSR223Adapter(ScriptEngineFactory factory) {
        this.factory = factory;
        ScriptEngine engine = factory.getScriptEngine();
        if (engine instanceof Compilable) {
            logger.debug("Enging [{}] detected as compilable.", factory.getEngineName());
            compiler = (Compilable) engine;
        } else {
            logger.debug("Enging [{}] detected as not compilable.", factory.getEngineName());
            compiler = new CompilableWrapper(engine);
        }
    }

    @Override
    public String[] types() {
        return new String[] { factory.getLanguageName() };
    }

    @Override
    public String[] extensions() {
        List<String> extensions = factory.getExtensions();
        return extensions.toArray(new String[extensions.size()]);
    }

    @Override
    public Object compile(String script) {
        try {
            return compiler.compile(script);
        } catch (ScriptException e) {
            logger.debug("Script failed compilation. Source: [{}]", e, script);
            return e;
        }
    }

    @Override
    public ExecutableScript executable(final Object script, @Nullable Map<String, Object> vars) {
        if (script instanceof CompiledScript) {
            return new ExecutableScriptAdapter((CompiledScript) script, vars);
        } else {
            throw new IllegalArgumentException("Unknown script: " + script.toString());
        }
    }

    @Override
    public SearchScript search(Object script, SearchLookup lookup, @Nullable Map<String, Object> vars) {
        if (script instanceof CompiledScript) {
            return new SearchScriptAdapter((CompiledScript) script, vars, lookup);
        } else {
            throw new IllegalArgumentException("Unknown script: " + script.toString());
        }
    }

    @Override
    public Object execute(Object script, Map<String, Object> vars) {
        if (script instanceof CompiledScript) {
            CompiledScript compiledScript = (CompiledScript) script;
            Bindings bindings = compiledScript.getEngine().createBindings();
            if (vars != null) {
                bindings.putAll(vars);
            }
            try {
                return compiledScript.eval(bindings);
            } catch (ScriptException e) {
                logger.debug("Script failed execution. Source: [{}]", e, script);
                return e;
            }
        } else {
            throw new IllegalArgumentException("Unknown script: " + script.toString());
        }
    }

    @Override
    public Object unwrap(Object value) {
        return value;
    }

    @Override
    public void close() {

    }
}
