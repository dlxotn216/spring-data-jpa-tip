package test.demo.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.demo.item.Item;

import javax.persistence.*;
import java.util.List;

/**
 * Created by taesu on 2018-07-13.
 */
@Entity
@Table(name = "APP_GROUP")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupSeqGen")
    @SequenceGenerator(name = "GroupSeqGen", sequenceName = "GROUP_DEQ")
    private Long groupKey;

    private String name;
    private String description;
    private Boolean deleted;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Item> items;

    public void addItem(Item item) {
        item.changeGroup(this);
        this.items.add(item);
    }

    public void removeItem(Item item) {
        item.removeGroup();
        this.items.remove(item);
    }

    public void deleteGroup() {
        this.deleted = true;
        this.items.forEach(Item::delete);
    }
}
