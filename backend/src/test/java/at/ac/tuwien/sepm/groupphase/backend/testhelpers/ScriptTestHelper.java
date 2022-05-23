package at.ac.tuwien.sepm.groupphase.backend.testhelpers;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleColorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ScriptMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

@Component
@Getter
public class ScriptTestHelper {
    @Autowired
    private ScriptMapper scriptMapper;

    public SimpleScriptDto dummySimpleScriptDtoWithoutColors() {
        return dummySimpleScriptDto(null);
    }

    public SimpleScriptDto dummySimpleScriptDto() {
        return dummySimpleScriptDto(Color.MAGENTA);
    }

    public SimpleScriptDto dummySimpleScriptDto(Color color) {
        final List<SimpleRoleDto> expectedRolesDto = new LinkedList<>();
        expectedRolesDto.add(new SimpleRoleDto("ALICE", new SimpleColorDto(color)));
        expectedRolesDto.add(new SimpleRoleDto("BOB", new SimpleColorDto(color)));
        expectedRolesDto.add(new SimpleRoleDto("MR. MISTER", new SimpleColorDto(color)));
        expectedRolesDto.add(new SimpleRoleDto("ANNA P.", new SimpleColorDto(color)));
        expectedRolesDto.add(new SimpleRoleDto("LADY MARI-MUSTER", new SimpleColorDto(color)));

        final List<SimpleLineDto> simpleLinesDto = new LinkedList<>();
        simpleLinesDto.add(
            new SimpleLineDto(
                0L,
                null,
                "Erster Akt",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                1L,
                null,
                "Das ist eine Beschreibung der Örtlichkeit, wo sich der erste Akt abspielt. Diese Phrase soll keiner Rolle zugewiesen werden.",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                2L,
                List.of(expectedRolesDto.get(0)),
                "Das ist die erste Phrase in diesem Theaterstück. Diese Phrase soll Alice zugeteilt werden.",
                true,
                null)
        );
        simpleLinesDto.add(
            new SimpleLineDto(
                3L,
                List.of(expectedRolesDto.get(1)),
                "Hallo Alice! Wie geht’s dir so? (Schaut Alice in die Augen)",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                4L,
                List.of(expectedRolesDto.get(2)),
                "Bla Bla Bla.",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                5L,
                null,
                "(Anna tritt auf.)",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                6L,
                List.of(expectedRolesDto.get(3)),
                "(fröhlich) Halli-hallöchen!",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                7L,
                List.of(expectedRolesDto.get(4)),
                "O man. Ich brauch‘ erst mal einen Kaffee.",
                true,
                Line.ConflictType.VERIFICATION_REQUIRED
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                8L,
                List.of(expectedRolesDto.get(3), expectedRolesDto.get(1)),
                "(gleichzeitig.) Ich auch!",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                9L,
                null,
                "Zweiter Akt",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                10L,
                null,
                "Vorhang",
                true,
                null
            ));

        simpleLinesDto.get(0).setIndex(0L);
        simpleLinesDto.get(1).setIndex(1L);
        simpleLinesDto.get(2).setIndex(2L);
        simpleLinesDto.get(3).setIndex(3L);
        simpleLinesDto.get(4).setIndex(4L);
        simpleLinesDto.get(5).setIndex(5L);
        simpleLinesDto.get(6).setIndex(6L);
        simpleLinesDto.get(7).setIndex(7L);
        simpleLinesDto.get(8).setIndex(8L);
        simpleLinesDto.get(9).setIndex(9L);
        simpleLinesDto.get(10).setIndex(10L);

        final List<SimplePageDto> simplePagesDto = new LinkedList<>();
        simplePagesDto.add(new SimplePageDto(simpleLinesDto, 0L));
        return new SimpleScriptDto("Skript_NF.pdf", simplePagesDto, expectedRolesDto);
    }

    public ScriptDto dummyScriptDto(Long id, SimpleUserDto owner) {
        return scriptMapper.simpleScriptDtoToScriptDto(dummySimpleScriptDto(), id, owner);
    }
}
