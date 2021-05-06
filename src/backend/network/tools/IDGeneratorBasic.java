package backend.network.tools;

import java.util.HashSet;
import java.util.Set;

/*A Basic ID generator which generate a random id and look up if the id is already in use
* @author nilschae
* @version 1.0 */
public class IDGeneratorBasic {
  private static Set<Integer> idsInUse = new HashSet<>();

  /*creates an ID random between 1 and the max value of an Integer
  * @return the id created*/
  public static Integer createID(){
    Integer id;
    do {
      id = Integer.valueOf((int)( Math.random()* Integer.MAX_VALUE) + 1);
    } while (!(idsInUse.add(id)));
    return id;
  }
}
