package com.cisco.darts.excel;

import com.cisco.darts.dto.Dart;

import java.io.File;
import java.util.List;

/**
 * User: Rost
 * Date: 28.04.2014
 * Time: 22:51
 */
public interface DartsExtractor {

    List<Dart> extract(File file);

}
