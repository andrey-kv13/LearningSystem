package students.dev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import students.dev.dto.CreateGroupRequest;
import students.dev.dto.CreateGroupResponse;
import students.dev.entity.Group;
import students.dev.exception.GroupNotFoundException;
import students.dev.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    private CreateGroupResponse toResponse(Group group) {
        return new CreateGroupResponse(
                group.getId(),
                group.getName()
        );
    }

    public Page<CreateGroupResponse> findAll(Pageable pageable) {
        return groupRepository.findAll(pageable).map(this::toResponse);

    }

    private Group findGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
    }

    public CreateGroupResponse getGroupById(Long id) {
        return toResponse(findGroupById(id));
    }

    public CreateGroupResponse createGroup(CreateGroupRequest request) {
        Group group = new Group();
        group.setName(request.name());
        return toResponse(groupRepository.save(group));
    }

    public CreateGroupResponse updateGroup(Long id, CreateGroupRequest request) {
        Group group = findGroupById(id);
        group.setName(request.name());
        return toResponse(groupRepository.save(group));
    }

    public void deleteGroup(Long id) {
        Group group = findGroupById(id);
        groupRepository.delete(group);
    }
}
