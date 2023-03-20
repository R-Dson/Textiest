package Data;

public class NPC {

    private final String name;
    private final int maxHP;
    public int hp;

    public NPC(String name, int maxHP)
    {
        this.name = name;
        this.maxHP = maxHP;


        hp = maxHP;
    }

}
