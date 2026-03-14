package tiger.service.facade;

import java.util.Collection;
import org.springframework.stereotype.Component;
import tiger.model.dto.OperationFields;
import tiger.model.requests.operation.*;
import tiger.service.OperationService;

@Component
public class OperationFacade {
    private final OperationService operationService;

    public OperationFacade(OperationService operationService) {
        this.operationService = operationService;
    }

    public void create(AddOperationRequest request) {
        operationService.addOperation(request.type(), request.accountId(), request.amount(),
                request.date(), request.description(), request.categoryId());
    }

    public void updateAmount(UpdateOperationAmountRequest request) {
        operationService.updateAmount(request.id(), request.newAmount());
    }

    public void updateCategory(UpdateOperationCategoryRequest request) {
        operationService.updateCategory(request.id(), request.newCategoryId());
    }

    public void updateDate(UpdateOperationDateRequest request) {
        operationService.updateDate(request.id(), request.newDate());
    }

    public void updateDescription(UpdateOperationDescriptionRequest request) {
        operationService.updateDescription(request.id(), request.newDescription());
    }

    public void delete(DeleteOperationRequest request) {
        operationService.deleteOperation(request.id());
    }

    public Collection<OperationFields> getAll() {
        return operationService.getAllOperations();
    }

    public Collection<OperationFields> getLastFive() {
        return operationService.getLastFiveOperations();
    }

    public OperationFields getOperation(GetOperationRequest request) {
        return operationService.getOperation(request.id());
    }
}
