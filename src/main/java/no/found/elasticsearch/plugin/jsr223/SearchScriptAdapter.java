package no.found.elasticsearch.plugin.jsr223;
import java.util.Map;

import javax.script.CompiledScript;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;


class SearchScriptAdapter extends ExecutableScriptAdapter implements SearchScript {
    
    private SearchLookup lookup;

    public SearchScriptAdapter(CompiledScript script, Map<String, Object> vars, SearchLookup lookup) {
        super(script, vars);
        this.lookup = lookup;
        bindings.putAll(lookup.asMap());
    }

    @Override
    public void setNextReader(AtomicReaderContext reader) {
        lookup.setNextReader(reader);
    }

    @Override
    public void setScorer(Scorer scorer) {
        lookup.setScorer(scorer);
    }

    @Override
    public void setNextDocId(int doc) {
        lookup.setNextDocId(doc);
    }

    @Override
    public void setNextSource(Map<String, Object> source) {
        lookup.source().setNextSource(source);
    }

    @Override
    public void setNextScore(float score) {
        bindings.put("_score", score);
    }

    @Override
    public float runAsFloat() {
        return ((Number) run()).floatValue();
    }

    @Override
    public long runAsLong() {
        return ((Number) run()).longValue();
    }

    @Override
    public double runAsDouble() {
        return ((Number) run()).doubleValue();
    }
    
}