package at.ac.tuwien.sepm.groupphase.backend.testhelpers;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SimpleScriptMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Getter
public class ScriptTestHelper {
    @Autowired
    private SimpleScriptMapper simpleScriptMapper;

    public SimpleScriptDto dummySimpleScriptDto() {
        final List<SimpleRoleDto> expectedRolesDto = new LinkedList<>();
        expectedRolesDto.add(new SimpleRoleDto("ALICE", null));
        expectedRolesDto.add(new SimpleRoleDto("BOB", null));
        expectedRolesDto.add(new SimpleRoleDto("MR. MISTER", null));
        expectedRolesDto.add(new SimpleRoleDto("ANNA P.", null));
        expectedRolesDto.add(new SimpleRoleDto("LADY MARI-MUSTER", null));

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
                List.of(new SimpleRoleDto[] { new SimpleRoleDto("ALICE", null) }),
                "Das ist die erste Phrase in diesem Theaterstück. Diese Phrase soll Alice zugeteilt werden.",
                true,
                null)
        );
        simpleLinesDto.add(
            new SimpleLineDto(
                3L,
                List.of(new SimpleRoleDto[] { new SimpleRoleDto("BOB", null) }),
                "Hallo Alice! Wie geht’s dir so? (Schaut Alice in die Augen)",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                4L,
                List.of(new SimpleRoleDto[] { new SimpleRoleDto("MR. MISTER", null) }),
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
                List.of(new SimpleRoleDto[] { new SimpleRoleDto("ANNA P.", null) }),
                "(fröhlich) Halli-hallöchen!",
                true,
                null
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                7L,
                List.of(new SimpleRoleDto[] { new SimpleRoleDto("LADY MARI-MUSTER", null) }),
                "O man. Ich brauch‘ erst mal einen Kaffee.",
                true,
                Line.ConflictType.VERIFICATION_REQUIRED
            ));
        simpleLinesDto.add(
            new SimpleLineDto(
                8L,
                List.of(new SimpleRoleDto[] { new SimpleRoleDto("ANNA P.", null), new SimpleRoleDto("BOB", null) }),
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
        return new SimpleScriptDto("file", simplePagesDto, expectedRolesDto);
    }

    public ScriptDto dummyScriptDto(Long id, SimpleUserDto owner) {
        return simpleScriptMapper.simpleScriptDtoToScriptDto(dummySimpleScriptDto(), id, owner);
    }
}
