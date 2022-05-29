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
import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.IllegalFileFormatException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidTokenException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnprocessableEmailException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.MailSender;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.SecureTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.UnparsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.ScriptParser;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.impl.ScriptParserImpl;
import at.ac.tuwien.sepm.groupphase.backend.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    private final AuthorizationService authorizationService;
    private final UserValidation userValidation;
    private final SecureTokenService secureTokenService;
    private final MailSender mailSender;

    @Autowired
    public ScriptServiceImpl(
        ScriptMapper scriptMapper,
        UserMapper userMapper,
        ScriptRepository scriptRepository,
        UserRepository userRepository, PageRepository pageRepository,
        LineRepository lineRepository,
        RoleRepository roleRepository,
        AuthorizationService authorizationService,
        UserValidation userValidation,
        SecureTokenService secureTokenService,
        MailSender mailSender
    ) {
        this.scriptMapper = scriptMapper;
        this.userMapper = userMapper;
        this.scriptRepository = scriptRepository;
        this.userRepository = userRepository;
        this.pageRepository = pageRepository;
        this.lineRepository = lineRepository;
        this.roleRepository = roleRepository;
        this.authorizationService = authorizationService;
        this.userValidation = userValidation;
        this.secureTokenService = secureTokenService;
        this.mailSender = mailSender;
    }

    @Transactional
    @Override
    public SimpleScriptDto parse(MultipartFile file, Integer startPage) {
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

        return scriptMapper.parsedScriptToSimpleScriptDto(parsedScript, file.getOriginalFilename());
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

    @Transactional
    @Override
    public ScriptDto save(SimpleScriptDto simpleScriptDto) {
        log.trace("save(scriptDto = {})", simpleScriptDto);

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }

        Script script;
        script = Script.builder()
            .name(simpleScriptDto.getName())
            .owner(user)
            .build();
        script = scriptRepository.save(script);
        Set<Role> roles = new HashSet<>();
        if (simpleScriptDto.getRoles() != null) {
            for (SimpleRoleDto roleDto : simpleScriptDto.getRoles()) {
                Role role = Role.builder()
                    .script(script)
                    .name(roleDto.getName())
                    .color(roleDto.getColor() == null ? null : roleDto.getColor().asColor())
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
        return scriptMapper.simpleScriptDtoToScriptDto(simpleScriptDto, script.getId(), owner);
    }

    @Override
    @Transactional
    public Stream<ScriptPreviewDto> findAllPreviews() {
        log.trace("getAllPreviews()");

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }


        List<ScriptPreviewDto> scripts = new ArrayList<>();
        List<Script> ownedScripts = scriptRepository.getScriptByOwner(user);
        for (Script s : ownedScripts) {
            scripts.add(scriptMapper.scriptToScriptPreviewDto(s, true));
        }

        List<Script> joinedScripts = scriptRepository.getScriptByParticipant(user);
        for (Script s : joinedScripts) {
            scripts.add(scriptMapper.scriptToScriptPreviewDto(s, false));
        }

        return scripts.stream();
    }

    @Override
    @Transactional
    public ScriptDto findById(Long id) {
        log.trace("getById(id = {})", id);
        authorizationService.checkMemberAuthorization(id);

        Optional<Script> script = scriptRepository.findById(id);
        if (script.isEmpty()) {
            throw new NotFoundException();
        }

        return scriptMapper.scriptToScriptDto(script.get());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.trace("delete(id = {})", id);

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Script> script = scriptRepository.findById(id);
        if (script.isPresent() && !script.get().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("Dieser User ist nicht berechtigt diese Datei zu löschen");
        }
        scriptRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ScriptDto patch(ScriptDto scriptDto, Long id) {
        log.trace("patch(id = {})", id);

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Script> script = scriptRepository.findById(id);
        if (script.isEmpty()) {
            throw new NotFoundException();
        }
        //TODO: fertig machen
        return null;
    }

    @Override
    @Transactional
    public ScriptDto getBySessionId(Long id) {
        log.trace("getBySessionId(id = {})", id);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Script script = scriptRepository.getScriptBySessionId(id);
        if (!script.getOwner().getId().equals(user.getId()) && !script.getParticipants().contains(user)) {
            throw new UnauthorizedException("User not allowed to view this script");
        }
        return scriptMapper.scriptToScriptDto(script);
    }

    @Override
    public void invite(Long scriptId, String email) {
        log.trace("invite(scriptId = {}, email = {})", scriptId, email);

        authorizationService.isOwnerOfScript(scriptId);

        Optional<Script> script = scriptRepository.findById(scriptId);
        if (script.isPresent()) {
            SecureToken secureToken = secureTokenService.createSecureToken(TokenType.INVITE_PARTICIPANT, 1440);
            secureToken.setScript(script.get());
            secureTokenService.saveSecureToken(secureToken);

            final String link = "http://localhost:4200/#/scripts/" + script.get().getId() + "/join/" + secureToken.getToken();
            try {
                mailSender.sendMail(email, "Aktoria Einladung",
                    """
                            <h1>Hallo,</h1>
                            %s lädt dich ein bei dem Aktoria Skript "%s" mitzulernen.
                            <br>
                            klick auf den folgenden Link, um dem Projekt beizutreten.
                            <br>
                            <a href='%s'>Beitreten</a>
                        """
                        .formatted(script.get().getOwner().getFirstName(), script.get().getName(), link));
            } catch (UnprocessableEmailException e) {
                throw new ServiceException(e.getMessage(), e);
            }

        } else {
            throw new NotFoundException();
        }
    }

    @Override
    @Transactional
    public void addParticipant(Long id, String token) {
        log.trace("addParticipant(id = {}, token = {})", id, token);

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }

        SecureToken secureToken = secureTokenService.findByToken(token);
        secureTokenService.removeToken(token);
        if (secureToken.getType() == TokenType.INVITE_PARTICIPANT) {
            if (secureToken.getExpireAt().isAfter(LocalDateTime.now())) {

                Script script = secureToken.getScript();
                if (script == null) {
                    throw new UnauthorizedException();
                }
                if (!Objects.equals(script.getId(), id)) {
                    throw new UnauthorizedException();
                }
                if (script.getOwner().getId().equals(user.getId())) {
                    throw new ConflictException("Du kannst nicht deinem eigenen Skript nochmal beitreten!");
                }
                if (script.getParticipants().contains(user)) {
                    throw new ConflictException("Du bist diesem Skript bereits begetreten!");
                }

                Set<Script> scripts = user.getParticipatesIn();
                scripts.add(script);
                user.setParticipatesIn(scripts);
                userRepository.saveAndFlush(user);

                Set<User> users = script.getParticipants();
                users.add(user);
                script.setParticipants(users);
                scriptRepository.saveAndFlush(script);
                return;
            }
        }
        throw new InvalidTokenException();
    }

    @Override
    public String inviteLink(Long scriptId) {
        log.trace("invite(scriptId = {})", scriptId);

        authorizationService.isOwnerOfScript(scriptId);

        Optional<Script> script = scriptRepository.findById(scriptId);
        if (script.isPresent()) {
            SecureToken secureToken = secureTokenService.createSecureToken(TokenType.INVITE_PARTICIPANT, 1440);
            secureToken.setScript(script.get());
            secureTokenService.saveSecureToken(secureToken);

            return "http://localhost:4200/#/scripts/" + script.get().getId() + "/join/" + secureToken.getToken();
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    @Transactional
    public void deleteParticipant(Long scriptId, String email) {
        authorizationService.checkMemberAuthorization(scriptId, email);

        Optional<User> userOpt = userRepository.findByEmail(email);
        Optional<Script> scriptOpt = scriptRepository.findById(scriptId);

        if (scriptOpt.isPresent() && userOpt.isPresent()) {
            Script script = scriptOpt.get();
            User user = userOpt.get();

            if (script.getOwner().getId().equals(user.getId())) {
                Set<User> participants = script.getParticipants();
                if (participants.isEmpty()) {
                    delete(scriptId);
                    return;
                }
                User newOwner = participants.iterator().next();
                participants.remove(newOwner);
                script.setOwner(newOwner);
                return;
            }

            Set<Script> scripts = user.getParticipatesIn();
            scripts.remove(script);
            user.setParticipatesIn(scripts);
            userRepository.saveAndFlush(user);

            Set<User> participants = script.getParticipants();
            participants.remove(user);
            script.setParticipants(participants);
            scriptRepository.saveAndFlush(script);
            return;
        }
        throw new NotFoundException();
    }
}
