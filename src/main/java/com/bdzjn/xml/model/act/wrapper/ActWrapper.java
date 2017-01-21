package com.bdzjn.xml.model.act.wrapper;


import com.bdzjn.xml.model.act.Act;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wrapper")
public class ActWrapper {

    @XmlElements({
            @XmlElement(name = "act", type = Act.class)
    })
    private List<Act> acts;

    public ActWrapper() {
    }

    public ActWrapper(List<Act> acts) {
        this.acts = acts;
    }

    public List<Act> getActs() {
        return acts;
    }

    public void setActs(List<Act> acts) {
        this.acts = acts;
    }
}
