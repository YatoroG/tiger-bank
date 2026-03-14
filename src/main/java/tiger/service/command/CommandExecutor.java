package tiger.service.command;

import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {
    public void execute(Command command) {
        long startTime = System.nanoTime();
        try {
            command.execute();
            double duration = (System.nanoTime() - startTime) / 1000000.0;
            System.out.printf("[%.3f мс] Команда '%s' выполнена%n",
                    duration, command.getName());
        } catch (Exception e) {
            System.out.println(command.getName() + ": " + e.getMessage());
            throw new RuntimeException("Не удалось выполнить команду: " + command.getName(), e);
        }
    }
}
