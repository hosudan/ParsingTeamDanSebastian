package ubbcluj.Model;

public class Action {
    private ActionType type;
    private int stindex;

    public Action(ActionType type, int index) {
        this.type = type;
        this.stindex = index;
    }

    @Override
    public String toString() {
        return type + " " + (type == ActionType.ACCEPT ? "":stindex);
    }

    public ActionType getType() {
        return type;
    }

    public int getStindex() {
        return stindex;
    }
}
