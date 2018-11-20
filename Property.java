package gravity;

public class Property<T>{
	T value;
	public Property(T value){
		this.value = value;
	}

	public T get(){
		return value;
	}

	public void set(T value){
		this.value = value;
	}

	@Override
	public String toString(){
		return value.toString();
	}
}
