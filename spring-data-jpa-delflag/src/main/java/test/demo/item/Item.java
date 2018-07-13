package test.demo.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.demo.group.Group;

import javax.persistence.*;

/**
 * Created by taesu on 2018-07-13.
 */
@Entity
@Table(name = "APP_ITEM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ItemSeqGen")
    @SequenceGenerator(name = "ItemSeqGen", sequenceName = "ITEM_DEQ")
    private Long itemKey;
    private String name;
    private String description;
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "GROUP_KEY")
    private Group group;

    public void changeGroup(Group group) {
        if (this.group != null) {
            this.group.removeItem(this);
        }
        this.group = group;
    }

    public void removeGroup() {
        this.group = null;
    }

    public void delete() {
        this.deleted = true;
    }
}
