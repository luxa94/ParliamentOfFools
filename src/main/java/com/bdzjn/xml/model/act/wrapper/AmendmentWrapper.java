package com.bdzjn.xml.model.act.wrapper;

import com.bdzjn.xml.model.act.Amendment;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wrapper")
public class AmendmentWrapper {

    @XmlElements({
            @XmlElement(name = "amendment", type = Amendment.class)
    })
    private List<Amendment> acts;

    public AmendmentWrapper(List<Amendment> acts) {
        this.acts = acts;
    }

    public AmendmentWrapper() {
    }

    public List<Amendment> getActs() {
        return acts;
    }

    public void setActs(List<Amendment> acts) {
        this.acts = acts;
    }
}
