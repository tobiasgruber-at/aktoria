package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptPreviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ScriptMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.IllegalFileFormatException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.UnparsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.ScriptParser;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.impl.ScriptParserImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A specific implementation of ScriptService.
 *
 * @author Simon Josef Kreuzpointner
 */
@Service
@Slf4j
public class ScriptServiceImpl implements ScriptService {
    private final ScriptMapper scriptMapper;
    private final UserMapper userMapper;
    private final ScriptRepository scriptRepository;
    private final UserRepository userRepository;
    private final PageRepository pageRepository;
    private final LineRepository lineRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ScriptServiceImpl(ScriptMapper scriptMapper, UserMapper userMapper, ScriptRepository scriptRepository, UserRepository userRepository, PageRepository pageRepository,
                             LineRepository lineRepository, RoleRepository roleRepository) {
        this.scriptMapper = scriptMapper;
        this.userMapper = userMapper;
        this.scriptRepository = scriptRepository;
        this.userRepository = userRepository;
        this.pageRepository = pageRepository;
        this.lineRepository = lineRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public SimpleScriptDto parse(MultipartFile file, Integer startPage) throws ServiceException, IllegalFileFormatException {
        log.trace("newScript(pdfScript = {})", file);

        boolean isPdfFile;
        try {
            isPdfFile = isPdfFileType(file);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        if (!isPdfFile) {
            throw new IllegalFileFormatException("Illegales Dateiformat.");
        }

        UnparsedScript s = new UnparsedScript(file, startPage);
        String raw;

        try {
            raw = s.getFileContentsAsPlainText();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        ScriptParser parser = new ScriptParserImpl(raw);
        ParsedScript parsedScript = parser.parse();

        return scriptMapper.parsedScriptToSimpleScriptDto(parsedScript, file.getName());
    }

    /**
     * Checks if the given file is of type pdf.
     *
     * @see <a href="https://sceweb.sce.uhcl.edu/abeysekera/itec3831/labs/FILE%20SIGNATURES%20TABLE.pdf"></a>
     */
    private boolean isPdfFileType(MultipartFile file) throws IOException {
        log.trace("isPdfFileType(file = {})", file);

        byte[] data = file.getBytes();

        return (
            data.length >= 4
                && data[0] == 0x25
                && data[1] == 0x50
                && data[2] == 0x44
                && data[3] == 0x46)
            && (
            data.length >= 10
                && data[data.length - 6] == 0x0a
                && data[data.length - 5] == 0x25
                && data[data.length - 4] == 0x25
                && data[data.length - 3] == 0x45
                && data[data.length - 2] == 0x4f
                && data[data.length - 1] == 0x46)
            || (
            data.length >= 11
                && data[data.length - 7] == 0x0a
                && data[data.length - 6] == 0x25
                && data[data.length - 5] == 0x25
                && data[data.length - 4] == 0x45
                && data[data.length - 3] == 0x4f
                && data[data.length - 2] == 0x46
                && data[data.length - 1] == 0x0a)
            || (
            data.length >= 13
                && data[data.length - 9] == 0x0d
                && data[data.length - 8] == 0x2a
                && data[data.length - 7] == 0x25
                && data[data.length - 6] == 0x25
                && data[data.length - 5] == 0x45
                && data[data.length - 4] == 0x4f
                && data[data.length - 3] == 0x46
                && data[data.length - 2] == 0x0d
                && data[data.length - 1] == 0x0a)
            || (
            data.length >= 11
                && data[data.length - 7] == 0x0d
                && data[data.length - 6] == 0x25
                && data[data.length - 5] == 0x25
                && data[data.length - 4] == 0x45
                && data[data.length - 3] == 0x4f
                && data[data.length - 2] == 0x46
                && data[data.length - 1] == 0x0d);
    }

    @Override
    @Transactional
    public ScriptDto save(SimpleScriptDto simpleScriptDto) throws ServiceException {
        log.trace("save(scriptDto = {})", simpleScriptDto);

        // TODO: refactor
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail;
        if (auth.getPrincipal() instanceof String) {
            userEmail = (String) auth.getPrincipal();
        } else {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) (auth.getPrincipal());
            userEmail = user.getUsername();
        }
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new ServiceException("Authenticated user not found.");
        }

        Script script;
        script = Script.builder()
            .name(simpleScriptDto.getName())
            .owner(user.get())
            .build();
        script = scriptRepository.save(script);
        Set<Role> roles = new HashSet<>();
        if (simpleScriptDto.getRoles() != null) {
            for (SimpleRoleDto roleDto : simpleScriptDto.getRoles()) {
                Role role = Role.builder()
                    .script(script)
                    .name(roleDto.getName())
                    .color(roleDto.getColor())
                    .build();
                role = roleRepository.save(role);
                roles.add(role);
            }
        }
        if (simpleScriptDto.getPages() != null) {
            for (SimplePageDto pageDto : simpleScriptDto.getPages()) {
                Page page = Page.builder()
                    .script(script)
                    .index(pageDto.getIndex())
                    .build();
                page = pageRepository.save(page);
                if (pageDto.getLines() != null) {
                    for (SimpleLineDto lineDto : pageDto.getLines()) {
                        Set<Role> spokenBy = new HashSet<>();
                        if (lineDto.getRoles() != null) {
                            for (SimpleRoleDto roleDto : lineDto.getRoles()) {
                                Optional<Role> role = roles.stream().filter(r -> r.getName().equals(roleDto.getName())).findFirst();
                                role.ifPresent(spokenBy::add);
                            }
                        }
                        Line line = Line.builder()
                            .page(page)
                            .index(lineDto.getIndex())
                            .content(lineDto.getContent())
                            .spokenBy(spokenBy)
                            .active(lineDto.isActive())
                            .build();
                        lineRepository.save(line);
                    }
                }
            }
        }

        SimpleUserDto owner = userMapper.userToSimpleUserDto(script.getOwner());
        ScriptDto scriptDto = scriptMapper.simpleScriptDtoToScriptDto(simpleScriptDto, script.getId(), owner);
        return scriptDto;
    }

    @Override
    public Stream<ScriptPreviewDto> findAllPreviews() throws ServiceException {
        log.trace("getAllPreviews()");

        // TODO: refactor
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail;
        if (auth.getPrincipal() instanceof String) {
            userEmail = (String) auth.getPrincipal();
        } else {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) (auth.getPrincipal());
            userEmail = user.getUsername();
        }
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new ServiceException("Authenticated user not found.");
        }

        return scriptMapper.listOfScriptToListOfScriptPreviewDto(scriptRepository.getScriptByOwner(user.get())).stream();
    }

    @Override
    @Transactional
    public ScriptDto findById(Long id) throws ServiceException, NotFoundException {
        log.trace("getById(id = {})", id);

        Optional<Script> script = scriptRepository.findById(id);
        if (!script.isPresent()) {
            throw new NotFoundException();
        }
        ScriptDto scriptDto = scriptMapper.scriptToScriptDto(script.get());
        return scriptDto;
    }

    @Override
    public void delete(Long id) {
        log.trace("delete(id = {})", id);
        scriptRepository.deleteById(id);
    }
}
