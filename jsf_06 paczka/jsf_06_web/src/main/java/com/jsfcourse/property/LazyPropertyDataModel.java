package com.jsfcourse.property;

import java.util.HashMap;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import jakarta.el.ValueExpression;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import com.jsf.dao.PropertyDAO;
import com.jsf.entities.Property;

public class LazyPropertyDataModel extends LazyDataModel<Property> {
    private static final long serialVersionUID = 1L;
    private List<Property> datasource;
    private PropertyDAO propertyDAO;
    private String filterAddress;
    
    
    public LazyPropertyDataModel(PropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
    }

    @Override
    public Property getRowData(String rowKey) {
        Integer id = Integer.valueOf(rowKey);
        if (datasource != null) {
            for (Property prop : datasource) {
                if (prop.getIdProperty().equals(id)) {
                    return prop;
                }
            }
        }
        return null;
    }

    @Override
    public String getRowKey(Property property) {
        return property.getIdProperty().toString();
    }

    public List<Property> loadWithSort(int first, int pageSize, String sortField, SortOrder sortOrder,
                                       Map<String, FilterMeta> filters) {
        // Pobranie pełnej listy nieruchomości
        datasource = propertyDAO.getFullList();

        // Sortowanie, jeśli ustawiono pole sortowania
        if (sortField != null) {
            Comparator<Property> comparator = null;
            switch (sortField) {
                case "address":
                    comparator = Comparator.comparing(Property::getAddress, 
                        Comparator.nullsLast(String::compareToIgnoreCase));
                    break;
                case "price":
                    comparator = Comparator.comparing(Property::getPrice, 
                        Comparator.nullsLast(BigDecimal::compareTo));
                    break;
                case "type":
                    comparator = Comparator.comparing(Property::getType, 
                        Comparator.nullsLast(String::compareToIgnoreCase));
                    break;
                // Dodaj kolejne przypadki w zależności od innych pól do sortowania
            }
            if (comparator != null) {
                if (sortOrder == SortOrder.DESCENDING) {
                    comparator = comparator.reversed();
                }
                datasource.sort(comparator);
            }
        }

        int dataSize = datasource.size();
        this.setRowCount(dataSize);

        // Stronicowanie: zwróć fragment listy dla bieżącej strony
        if (dataSize > pageSize) {
            try {
                return datasource.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return datasource.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return datasource;
        }
    }

    @Override
    public int count(Map<String, FilterMeta> filters) {
        // Prosta implementacja ignorująca filtry
        return propertyDAO.getFullList().size();
    }

    @Override
    public List<Property> load(int first, int pageSize,
                               Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        String sortField = null;
        SortOrder sortOrder = SortOrder.UNSORTED;

        if (sortBy != null && !sortBy.isEmpty()) {
            SortMeta meta = sortBy.values().iterator().next();
            ValueExpression sortByExp = meta.getSortBy();
            if (sortByExp != null) {
                String exp = sortByExp.getExpressionString();
                // Zakładamy, że wyrażenie ma postać "#{p.nazwaPola}"
                if (exp.startsWith("#{") && exp.endsWith("}")) {
                    exp = exp.substring(2, exp.length() - 1); // usuń "#{...}"
                }
                int dotIndex = exp.indexOf('.');
                if (dotIndex != -1 && dotIndex < exp.length() - 1) {
                    sortField = exp.substring(dotIndex + 1);
                } else {
                    sortField = exp;
                }
            }
            sortOrder = meta.getOrder();
        }

        return loadWithSort(first, pageSize, sortField, sortOrder, filterBy);
    }
}
