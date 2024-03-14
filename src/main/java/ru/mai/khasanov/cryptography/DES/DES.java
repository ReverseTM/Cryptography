package ru.mai.khasanov.cryptography.DES;

import ru.mai.khasanov.cryptography.FeistelNetwork.FeistelNetwork;
import ru.mai.khasanov.cryptography.interfaces.IEncrypt;
import ru.mai.khasanov.cryptography.interfaces.IKeyExpand;

public class DES extends FeistelNetwork {
    public DES(IKeyExpand keyExtend, IEncrypt feistelFunction) {
        super(keyExtend, feistelFunction);
        this.rounds = 16;
    }
}
