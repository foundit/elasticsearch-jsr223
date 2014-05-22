package no.found.elasticsearch.plugin.jsr223;

import java.io.Reader;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

final class CompilableWrapper implements Compilable {
    private static abstract class AbstractCompiledScript extends CompiledScript {
        protected final ScriptEngine engine;

        public AbstractCompiledScript(ScriptEngine engine) {
            this.engine = engine;
        }
        
        @Override
        public final ScriptEngine getEngine() {
            return engine;
        }
    }
    private static final class StringCompiledScript extends AbstractCompiledScript {
        private final String script;

        private StringCompiledScript(String script, ScriptEngine engine) {
            super(engine);
            this.script = script;
        }


        @Override
        public Object eval(ScriptContext context) throws ScriptException {
            return engine.eval(script, context);
        }
    }

    private static final class ReaderCompiledScript extends AbstractCompiledScript {
        private final Reader script;

        private ReaderCompiledScript(Reader script, ScriptEngine engine) {
            super(engine);
            this.script = script;
        }

        @Override
        public Object eval(ScriptContext context) throws ScriptException {
            return engine.eval(script, context);
        }
    }

    private final ScriptEngine engine;
    
    public CompilableWrapper(ScriptEngine engine) {
        this.engine = engine;
    }
    @Override
    public CompiledScript compile(final Reader script) throws ScriptException {
        return new ReaderCompiledScript(script, engine);
    }

    @Override
    public CompiledScript compile(final String script) throws ScriptException {
        return new StringCompiledScript(script, engine);
    }
}