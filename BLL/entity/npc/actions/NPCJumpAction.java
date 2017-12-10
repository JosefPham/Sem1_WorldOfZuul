package BLL.entity.npc.actions;

public abstract class NPCJumpAction extends NPCAction {
	private int actionId;

	public NPCJumpAction(String msg, int actionId) {
		super(msg);
		this.actionId = actionId;
	}

	public NPCJumpAction(String msg) {
		this(msg, -1);
	}

	public int getActionId() {
		return actionId;
	}

	protected void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public void resetActionId() {
		this.actionId = -1;
	}
}
