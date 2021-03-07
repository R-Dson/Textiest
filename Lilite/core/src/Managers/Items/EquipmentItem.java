package Managers.Items;

public class EquipmentItem extends Item {
    //Type
    public ItemEnums.EquipmentType EquipmentType;

    //Attributes
    public int Intelligence;
    public int Strength;
    public int Dexterity;

    //Requirements
    public int IntelligenceReq;
    public int StrengthReq;
    public int DexterityReq;
    public int LevelReq;

    //Weapon
    public ItemEnums.WeaponItemType WeaponItemType;
    public int[] WeaponDamageRange;

}
