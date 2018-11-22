/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityapplication;

/**
 *
 * @author Hassan Mohammed
 */
public class Property<E> {
    private E value;
    
    public Property(E value){
        this.value = value;
    }
    
    public E get(){
        return value;
    }
    
    public void set(E value){
        this.value = value;
    }
    
    @Override
    public String toString(){
        return value.toString();
    }
}
