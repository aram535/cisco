package com.cisco.serials;

import com.cisco.serials.dto.Serial;
import com.google.common.base.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.apache.commons.lang3.StringUtils.split;

@Component("serialsImporter")
public class DefaultSerialsImporter implements SerialsImporter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Serial> importSerials(String serialsString) {

        List<String> serialsStrings = newArrayList(split(serialsString, "\n\r"));

        List<Serial> serials = newArrayList(transform(serialsStrings, new Function<String, Serial>() {
            @Override
            public Serial apply(String input) {
                return new Serial(input);
            }
        }));
        logger.info("imported serials: {}", serials);
        return serials;
    }
}
