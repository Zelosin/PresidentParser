package org.zelosin.Assumers.Abstract;

        import org.json.JSONObject;

public interface Assumer {
    void consume(Object key, Object value);
    void emptyConsume(Object key);
    void set(JSONObject jsonObject);
    JSONObject provideContent();
}
