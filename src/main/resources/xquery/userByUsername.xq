declare namespace u = "http://www.fools.gov.rs/users";
declare variable $username as xs:token external;

for $x in doc('/xml/users/xml-user.xml')/u:user
where $x/u:username=$username
return $x