package no.found.elasticsearch.plugin.jsr223;
import java.util.Map;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import org.elasticsearch.script.AbstractExecutableScript;


class ExecutableScriptAdapter extends AbstractExecutableScript {
    protected CompiledScript compiledScript;
    protected Bindings bindings;

    ExecutableScriptAdapter(CompiledScript script, Map<String, Object> vars) {
        compiledScript = script;
        bindings = compiledScript.getEngine().createBindings();
        if (vars != null) {
            bindings.putAll(vars);
        }
    }

    @Override
    public void setNextVar(String name, Object value) {
        bindings.put(name, value);
    }

    @Override
    public Object run() {
        try {
            return compiledScript.eval(bindings);
        } catch (ScriptException e) {
            return e;
        }
    }
}