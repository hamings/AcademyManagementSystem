package src.repository;

import src.ServiceType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryProvider {
    private static final List<Repository> repositoryList;
    private static RepositoryProvider instance;
    private Map<ServiceType, Repository> repositoryMap; // init방지

    static {
        repositoryList = new ArrayList<>();
        repositoryList.add(new StudentRepository());
        repositoryList.add(new LectureRepository());
        repositoryList.add(new LectureRegistrationRepository());
        repositoryList.add(new TeacherRepository());
        repositoryList.add(new NotificationRepository());
    }

    private RepositoryProvider() {}
    public static RepositoryProvider getInstance() {
        if(instance == null) {
            instance = new RepositoryProvider();
            instance.repositoryMap = new HashMap<>();
        }

        return instance;
    }

    public Repository provide(ServiceType serviceType) throws IOException {
        Repository repository;
        if((repository = repositoryMap.get(serviceType)) != null) {
            // 이미 init된 레포지토리 있는지 검색 -> 있으면 반환
            return repository;
        }


        repository = repositoryList.stream()
                .filter(repo ->  repo.support(serviceType))
                .findFirst()
                .get();

        repository.init();
        repositoryMap.put(serviceType, repository);

        return repository;
    }
}
