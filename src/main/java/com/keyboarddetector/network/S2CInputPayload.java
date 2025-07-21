package com.keyboarddetector.network;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.keyboarddetector.KeyboardDetector.KEY_PRESSED_PACKET_ID;

public record S2CInputPayload(Set<Integer> asciiCodes) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CInputPayload> ID = new CustomPacketPayload.Type<>(KEY_PRESSED_PACKET_ID);
    public static final StreamCodec<FriendlyByteBuf, S2CInputPayload> CODEC = StreamCodec.of(
        (buf, payload) -> {
            buf.writeByte(payload.asciiCodes().size());
            for (int key : payload.asciiCodes()) {
                buf.writeInt(key);
            }
        },
        buf -> {
            byte size = buf.readByte();
            Set<Integer> keys = new HashSet<>(size);
            for (int i = 0; i < size; i++) {
                keys.add(buf.readInt());
            }
            return new S2CInputPayload(keys);
        }
    );

    public Pair<Collection<Integer>, Collection<Integer>> matchMissingAndExtra(Collection<Integer> expected) {
        List<Integer> missing = new ArrayList<>();
        List<Integer> extra = new ArrayList<>();
        for (Integer key : expected) {
            if (!asciiCodes.contains(key)) {
                missing.add(key);
            }
        }
        for (Integer key : asciiCodes) {
            if (!expected.contains(key)) {
                extra.add(key);
            }
        }
        return Pair.of(missing, extra);
    }

    public boolean isKeyDown(int ascii) {
        return asciiCodes.contains(ascii);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
