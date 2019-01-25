package com.passive.api.runescape.trackers.skills;

import com.runemate.game.api.hybrid.local.Skill;

/**
 * Created by Taffy on 14/12/2015.
 */
public interface SkillEventListener {
    void onLevelUp(Skill skill, SkillTracker.SkillInfo info);

    void onExperienceGained(Skill skill, SkillTracker.SkillInfo info);

    void update(Skill skill, SkillTracker.SkillInfo info);

    void skillInfoAdded(Skill skill, SkillTracker.SkillInfo info);

    void skillInfoRemoved(Skill skill, SkillTracker.SkillInfo info);
}
