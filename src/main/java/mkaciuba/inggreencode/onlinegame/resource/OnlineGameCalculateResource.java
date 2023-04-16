package mkaciuba.inggreencode.onlinegame.resource;

import mkaciuba.inggreencode.onlinegame.dto.Clan;
import mkaciuba.inggreencode.onlinegame.dto.Players;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/onlinegame/calculate")
public class OnlineGameCalculateResource {

    @PostMapping
    public List<List<Clan>> calculateOrder(@RequestBody Players players) {
        var sortedClans = players.clans().stream()
                .sorted(
                        Comparator.comparing(Clan::points).reversed()
                                .thenComparing(Clan::numberOfPlayers)
                )
                .collect(Collectors.toCollection(LinkedList::new));

        var result = new ArrayList<List<Clan>>();
        var maxGroupSize = players.groupCount();

        while (!sortedClans.isEmpty()) {
            var groupSize = 0;
            var group = new ArrayList<Clan>();
            var iterator = sortedClans.iterator();

            while (iterator.hasNext() && groupSize < maxGroupSize) {
                var clan = iterator.next();
                if (groupSize + clan.numberOfPlayers() <= maxGroupSize) {
                    group.add(clan);
                    groupSize += clan.numberOfPlayers();
                    iterator.remove();
                }
            }

            result.add(group);
        }

        return result;
    }
}
