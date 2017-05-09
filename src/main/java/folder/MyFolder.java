package folder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;
import java.util.Set;

/**
 * Created by ruslan on 09.05.2017.
 */

@Data
@AllArgsConstructor
public class MyFolder {

    private String name;
    private Set<Path> nestedFiles;

}
