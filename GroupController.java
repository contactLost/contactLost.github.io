package com.example.People;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.Course.*;
@RestController
class GroupController {

    private final GroupRepository repository;
    private final PeopleRepository peopleRepository;
    
    public GroupController(GroupRepository repository, PeopleRepository peopleRepository) {
        this.repository = repository;
        this.peopleRepository = peopleRepository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/allGroups")
    List<Group> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/allGroups")
    Group newGroup(@RequestBody Group newGroup) {
        return repository.save(newGroup);
    }
    
    //Groups of a course
    @GetMapping("/coursefindgroup/{id}")
    List<Group> getGroupsOfCourse(@PathVariable Course id){
    	return repository.findByCourseOrderByName(id);
    }

    // Single item

    @GetMapping("/allGroups/{id}")
    Group one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
    }

    @PutMapping("/allGroups/{id}")
    Group replaceGroup(@RequestBody Group newGroup, @PathVariable Long id) {

        return repository.findById(id)
                .map(group -> {
                    group.setName(newGroup.getName());
                    group.setGroupNo(newGroup.getGroupNo());
                    group.setGroupGrade(newGroup.getGroupGrade());
                    group.setAllPeople(newGroup.getAllPeople());
                    group.setGroupTasks(newGroup.getGroupTasks());
                    return repository.save(group);
                })
                .orElseGet(() -> {
                    newGroup.setId(id);
                    return repository.save(newGroup);
                });
    }
    @PostMapping("/leaveGroup/{group}/{student}")
    void leaveGroup(@PathVariable Group group, People student) {
    	List<People> currentPeople = group.getAllPeople();
    	currentPeople.remove(student);
    	if(people.isEmpty()) {
    		repository.deleteById(group.getId());
    	}
    	else {
    		group.setAllPeople(currentPeople);
    	}
    }

    @PostMapping("/joinGroup/{group}/{student}")
    void joinGroup(@PathVariable Group group, People people) {
        List<People> currentPeople = group.getAllPeople();
        if ((group.getCourse()).getUnassignedStudents()).contains(people)) {
                currentPeople.add(people);
        }
        group.setAllPeople(currentPeople);
    }

    @PostMapping("/joinGroup/{group}/{student1}{student2}")
    void joinGroupWithFriend(@PathVariable Group group, List<People> newPeople ) {
        List<People> currentPeople = group.getAllPeople();
        for (People people : newPeople) {
            if ((group.getCourse()).getUnassignedStudents()).contains(people)) {
                currentPeople.add(people);
            }
        }
        group.setAllPeople(currentPeople);
    }

    @DeleteMapping("/allGroups/{id}")
    void deleteGroup(@PathVariable Long id) {
        repository.deleteById(id);
    }
}