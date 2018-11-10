import java.util.Hashtable;
import java.util.Map;

/**
 * 多key的hash
 * @author Administrator
 * 普通的hash只有一对key、value值
 * 该类型的对象可以通过多个key（带type的）找到相同的value
 * 
 */
public class LZZMultiKeyHash<T> {
	private Map<String, Map<String, String>> keyMap = null;
	private Map<String, T> realMap = null;
	
	/**
	 * 通过type和key获取value
	 * @param type 类型
	 * @param key key值
	 * @return value值
	 */
	public T get(String type, String key){
		if(null==type
				|| "".equals(type)){
			return get(key);
		}
		
		if(null==keyMap.get(type)) return null;
		
		if(null==keyMap.get(type).get(key)) return null;
		
		return realMap.get(keyMap.get(type).get(key));
	}
	
	/**
	 * 通过原始key查找对象
	 * @param key 原始key
	 * @return value值
	 */
	public T get(String key){
		if(null==key
				|| "".equals(key)){
			return null;
		}
		
		return realMap.get(key);
	}
	
	/**
	 * 添加一组新的对应
	 * @param type 新的对应关系的类型名称
	 * @param key_map 新的key值与原始key值之间的对应map
	 * @return
	 */
	public boolean addKeyMap(String type, Map<String, String> key_map){
		if(null==realMap){
			//没有配置主Hash
			return false;
		}
		
		if(null==keyMap) keyMap = new Hashtable<String, Map<String,String>>();
		
		if(null==type 
				|| "".equals(type)){
			//无效的Type
			return true;
		}
		
		if(null!=keyMap.get(type)){
			//该type已经存在
			keyMap.get(type).putAll(key_map);
		}else{
			keyMap.put(type, key_map);
		}
		
		return true;
	}
	
	/**
	 * 添加一条单独的LZZMultiKeyHash记录
	 * @param type 对应关系类型名称
	 * @param type_key 类型的key值
	 * @param key 原始key值
	 * @param value 原始value值
	 * @return 是否添加成功
	 */
	public boolean put(String type, String type_key, String key, T value){
		if(null==type){
			return put(key, value);
		}
		
		if(null==type_key
				|| "".equals(type_key)
				|| null==key
				|| "".equals(key)){
			return false;
		}
		
		put(type, type_key, key);
		put(key, value);
		
		return true;
	}
	
	/**
	 * 向type类型对应的hash中添加一个key的映射
	 * @param type
	 * @param type_key
	 * @param orig_key
	 * @return
	 */
	public boolean put(String type, String type_key, String orig_key){
		if(null==type
				|| "".equals(type)){
			return true;
		}
		
		if(type_key==null
				|| "".equals(type_key)){
			//无效的key
			return true;
		}
		
		if(null==keyMap){
			keyMap = new Hashtable<String, Map<String, String>>();
		}
		
		if(null==keyMap.get(type)){
			Map map = new Hashtable<String, String>();
			map.put(type_key, orig_key);
			keyMap.put(type, map);
			return true;
		}
		
		keyMap.get(type).put(type_key, orig_key);
		
		return true;
	}
	
	/**
	 * 添加一组原始的key value
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(String key, T value){
		if(key==null
				|| "".equals(key)){
			//无效的key
			return true;
		}
		
		if(null==realMap) realMap = new Hashtable<String, T>();
		
		realMap.put(key, value);
		
		return true;
	}
	
	/**
	 * 清除对象所有内容
	 */
	public void clear(){
		if(null!=realMap) realMap.clear();
		if(null!=keyMap) keyMap.clear();
	}
	
	/**
	 * 清除子hash
	 * @param type hash的type
	 */
	public void clear(String type){
		if(null==keyMap) return;
		
		if(null==keyMap.get(type)) return;
		
		keyMap.get(type).clear();
	}
}


