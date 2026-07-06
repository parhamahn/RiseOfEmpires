package model.unit;

public class BorderExpander extends Unit{

    public BorderExpander(){
        super(UNIT_TYPE.BORDER_EXPANDER, 10, 1);
    }

    @Override
    public boolean isBorderExpander() { return true; }
}
