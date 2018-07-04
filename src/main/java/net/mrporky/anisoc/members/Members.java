package net.mrporky.anisoc.members;

import java.util.Comparator;
import java.util.TreeSet;

public class Members {

    private TreeSet<Member> members = new TreeSet<Member>(new MemberComp());

    public Member getMember(String uniqueID) throws MemberNotFoundException{
        for(Member member : members){
            if(member.getUniqueID().equalsIgnoreCase(uniqueID)){
                return member;
            }else if(member.getUniqueID().compareTo(uniqueID) > 0) {
                break;
            }
        }
        throw new MemberNotFoundException();
    }

    public void add(Member member) {
        members.add(member);
    }

    private class MemberComp implements Comparator<Member> {
        @Override
        public int compare(Member member1, Member member2) {
            return member1.getUniqueID().compareTo(member2.getUniqueID());
        }
    }
}
