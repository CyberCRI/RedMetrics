
package org.cri.configurator;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

/**
 *
 * @author TTAK
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public interface Config<K, V extends Serializable> {
    
    public void put(K key, V value);
    
    public V get(String K);
    
    public void read(Path path) throws IOException;
    
    public void write(Path path) throws IOException;
    
}
