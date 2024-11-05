
package org.openweathermap.api;

import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.annotation.Generated;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({
    "1h"
})
@Generated("jsonschema2pojo")
public class Rain {

    @JsonbProperty("1h")
    private Double _1h;
    @JsonbTransient
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonbProperty("1h")
    public Double get1h() {
        return _1h;
    }

    @JsonbProperty("1h")
    public void set1h(Double _1h) {
        this._1h = _1h;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
