package me.rezscripts.rpgexperience.options;

import java.util.HashMap;
import java.util.Map.Entry;

public class OptionsList {

    public HashMap<RPGOption, Boolean> options = new HashMap<RPGOption, Boolean>();

    public OptionsList(String s) {
        String[] data = s.split(" ");
        for (String a : data) {
            if (a.length() == 0)
                continue;
            try {
                String[] data2 = a.split("::");
                RPGOption so = RPGOption.get(data2[0]);
                boolean val = Boolean.parseBoolean(data2[1]);
                if (so != null) {
                    options.put(so, val);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<RPGOption, Boolean> e : options.entrySet()) {
            sb.append(e.getKey().toString());
            sb.append("::");
            sb.append(e.getValue());
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public boolean get(RPGOption so) {
        if (options.containsKey(so))
            return options.get(so);
        options.put(so, so.getDefault());
        return so.getDefault();
    }

    public boolean toggle(RPGOption so) {
        boolean s = get(so);
        s = !s;
        options.put(so, s);
        return s;
    }

}