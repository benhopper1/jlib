package hopper.exec.passable;

import java.io.Serializable;

public class TransportObject implements Serializable {
	public  ExecObject execObject;
	public TransportObject(ExecObject inExecObject) {
		execObject = inExecObject;
	}
}
