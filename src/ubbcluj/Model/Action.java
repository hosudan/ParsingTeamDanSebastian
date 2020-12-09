package ubbcluj.Model;

public class Action {
    private ActionType type;
    private int stindex;

    public void setType(ActionType type) {
        this.type = type;
    }

    public void setStindex(int stindex) {
        this.stindex = stindex;
    }

    public Action(ActionType type, int index) {
        this.type = type;
        this.stindex = index;
    }

    @Override
    public String toString() {
        return type + " " + (type == ActionType.REDUCE ? "":stindex);
    }

    public ActionType getType() {
        return type;
    }

    public int getStindex() {
        return stindex;
    }
}
