package view.game.map.disasters.flood;

import view.game.map.disasters.water.WaterEffectLayer;

import java.awt.*;

public class FloodEffectLayer extends WaterEffectLayer {

    private static final int RISE_TICKS = 45;
    private static final int HOLD_TICKS = 70;
    private static final int RECEDE_TICKS = 45;
    private static final int STAGGER_PER_RING = 12;

    private static final Color DEEP = new Color(55, 85, 70, 190);
    private static final Color SHALLOW = new Color(120, 140, 95, 140);
    private static final Color FOAM = new Color(205, 215, 185, 200);

    @Override protected int riseTicks() { return RISE_TICKS; }
    @Override protected int holdTicks() { return HOLD_TICKS; }
    @Override protected int recedeTicks() { return RECEDE_TICKS; }
    @Override protected int staggerPerRing() { return STAGGER_PER_RING; }
    @Override protected Color deepColor() { return DEEP; }
    @Override protected Color shallowColor() { return SHALLOW; }
    @Override protected Color foamColor() { return FOAM; }
    @Override protected double waveAmplitude() { return 3.0; }
    @Override protected double waveSpeed() { return 0.12; }
}