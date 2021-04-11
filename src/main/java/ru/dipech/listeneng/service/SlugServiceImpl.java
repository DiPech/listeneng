package ru.dipech.listeneng.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SlugServiceImpl implements SlugService {

    @Override
    public String slugify(String input) {
        // Decided not to waste time on transliterations from any language into latin,
        // so just use hash for uniquely identifying.
        return UUID.nameUUIDFromBytes(input.getBytes()).toString();
    }

}
