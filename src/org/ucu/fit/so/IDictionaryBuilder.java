package org.ucu.fit.so;

import java.util.HashMap;

public interface IDictionaryBuilder {

    HashMap<String, Object> buildDictionary(String filePath);
}
