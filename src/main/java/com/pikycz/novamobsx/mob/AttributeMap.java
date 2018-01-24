package com.pikycz.novamobsx.mob;

import cn.nukkit.entity.Attribute;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jaroslav Pikart
 */
public class AttributeMap {

    protected Map<Integer, Attribute> attributes = new HashMap<>();

    public void addAttribute(Attribute attribute) {
        this.attributes.put(attribute.getId(), attribute);
    }

    public Attribute getAttribute(int id) {
        if (!this.attributes.containsKey(id)) {
            return null;
        }
        return this.attributes.get(id);
    }

    public Map<Integer, Attribute> getAll() {
        return this.attributes;
    }
}
